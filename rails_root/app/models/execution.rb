class Execution < ActiveRecord::Base
  belongs_to :plan
  has_many :activities
  has_many :habits, :through => :plan

  def actable?
    self.date == Date.today or self.date == Date.today - 1
  end
  
  def act!(habit)
    self.activities.create!(:habit_id => habit.id)
  end
  
  def deact!(habit)
    self.activities.find_all_by_habit_id(habit.id).each{|activity| activity.destroy }
  end
  
  class Status
    GREAT = 'great'
    GOOD = 'good'
    BAD = 'bad'
    UNKNOWN = 'unknown'
    
    def self.list
      [GREAT, GOOD, BAD, UNKNOWN]
    end
  end
  
  def status
    return Status::UNKNOWN if self.date > Date.today
    if acted_habits.blank?
      return self.date == Date.today ? Status::UNKNOWN : Status::BAD 
    end
    return Status::GREAT if acted_habits.size == self.habits.size
    return Status::GOOD
  end
  
  def status_of(habit)
    acted?(habit) ? 'acted' : 'not_acted'
  end
  
  def acted?(habit = nil)
    return (not acted_habits.blank?) if habit.nil?
    not self.activities.find_by_habit_id(habit).blank?
  end
  
  private
  def acted_habits
    habits.select{|habit| acted?(habit) }
  end
end
