class Habit < ActiveRecord::Base
  belongs_to :plan
  has_many :activities
  
  def summary
    days = acted_days
    "#{short_title} (#{days} #{days <= 1 ? 'day' : 'days'})"
  end  
  
  def short_title
    return title unless title.length > 20
    title[0...20] + "..."
  end
  
  private
  def acted_days
    plan.executions.select{|execution| execution.acted?(self) }.size
  end
end
