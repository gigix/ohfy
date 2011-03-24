class User < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :token_authenticatable, :confirmable, :lockable and :timeoutable
  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :trackable, :validatable

  # Setup accessible (or protected) attributes for your model
  attr_accessible :email, :password, :password_confirmation, :remember_me
  
  has_many :plans, :include => {:executions => :activities}
  
  def name
    self.email.split('@').first
  end
  
  def create_plan!(from_date, habit_names)    
    habit_names.reject!(&:blank?)
    raise if habit_names.blank?
    
    from_date = Date.parse(from_date) if(from_date.is_a?(String))
    
    plan = self.plans.build(:start_from => from_date)

    self.active_plans.each {|plan| plan.abandon!} if covered_by?(plan)

    plan.status = Plan::Status::ACTIVE
    plan.save!
    
    habit_names.each do |name|
      habit = plan.habits.create!(:title => name)
    end
    
    return plan
  end
  
  def active_plans
    plans.select{|plan| plan.active?}
  end
  
  def current_plan
    active_plans.detect do |plan|
      covered_by?(plan)
    end
  end 
  
  def time_zone
    ActiveSupport::TimeZone.new(time_zone_name)
  end 
  
  def today
    time_zone.utc_to_local(Time.now).to_date
  end
  
  def execution_on_today
    current_plan.execution_on(today)
  end
  
  private
  def covered_by?(plan)
    plan.covers?(Date.today)
  end
end
