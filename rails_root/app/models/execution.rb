class Execution < ActiveRecord::Base
  belongs_to :plan
  has_many :activities
  has_many :habits, :through => :plan
  
  def act!(habit_id)
    self.activities.create!(:habit_id => habit_id)
  end
  
  class Status
    GREAT = 'great'
    GOOD = 'good'
    BAD = 'bad'
    UNKNOWN = 'unknown'
  end
  
  def status
    return Status::UNKNOWN if self.date > Date.today
    acted_habits = self.habits.select{|habit| acted?(habit) }
    if acted_habits.blank?
      return self.date == Date.today ? Status::UNKNOWN : Status::BAD 
    end
    return Status::GREAT if acted_habits.size == self.habits.size
    return Status::GOOD
  end
  
  private
  def acted?(habit)
    not self.activities.find_by_habit_id(habit).blank?
  end
end
