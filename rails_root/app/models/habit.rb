class Habit < ActiveRecord::Base
  belongs_to :plan
  has_many :activities
  
  def summary
    days = acted_days
    "#{title} (#{days} #{days <= 1 ? 'day' : 'days'})"
  end  
  
  private
  def acted_days
    plan.executions.select{|execution| execution.acted?(self) }.size
  end
end
