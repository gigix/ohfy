class Execution < ActiveRecord::Base
  belongs_to :plan
  has_many :activities
  has_many :habits, :through => :plan

  def description
    date_str = "#{date.to_s(:db)}: "
    activities.blank? ? 
      date_str + "did nothing." :
      date_str + activities.map{|activity| activity.habit.title}.join("; ") + "."
  end

  def actable?
    self.date == plan.today or self.date == plan.today - 1
  end
  
  def act!(habit)
    self.activities.create!(:habit_id => habit.id)
  end
  
  def deact!(habit)
    self.activities.find_all_by_habit_id(habit.id).each{|activity| activity.destroy }
    self.reload
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
    return Status::UNKNOWN if self.date > plan.today
    if acted_habits.blank?
      return self.date == plan.today ? Status::UNKNOWN : Status::BAD 
    end
    return Status::GREAT if acted_habits.size == self.habits.size
    return Status::GOOD
  end
  
  def status_of(habit)
    acted?(habit) ? 'acted' : 'not_acted'
  end
  
  def acted?(habit = nil)
    return (not acted_habits.blank?) if habit.nil?
    self.activities.detect{|activity| activity.habit == habit}
  end
  
  private
  def acted_habits
    habits.select{|habit| acted?(habit) }
  end
end
