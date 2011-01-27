class Plan < ActiveRecord::Base
  belongs_to :user
  has_many :habits
  has_many :executions
  
  after_create :create_executions
  
  def execution_on(date)
    executions.detect{|execution| execution.date == date}
  end
  
  class Status
    ACTIVE = 'active'
    ABANDONED = 'abandoned'
  end
  
  def active?
    status == Status::ACTIVE
  end
  
  def abandon!
    update_attribute(:status, Status::ABANDONED)
  end
  
  private
  def create_executions
    (0...30).each do |how_many|
      executions.create!(:date => start_from + how_many.days)
    end
  end
end
