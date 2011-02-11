class User < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :token_authenticatable, :confirmable, :lockable and :timeoutable
  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :trackable, :validatable

  # Setup accessible (or protected) attributes for your model
  attr_accessible :email, :password, :password_confirmation, :remember_me
  
  has_many :plans
  
  def create_plan!(from_date, habit_names)    
    raise if habit_names.blank?
    
    from_date = Date.parse(from_date) if(from_date.is_a?(String))
    
    self.active_plans.each {|plan| plan.abandon!}

    plan = self.plans.create!(:start_from => from_date, :status => Plan::Status::ACTIVE)
    
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
      Date.today >= plan.start_from and (Date.today - plan.start_from) < 30
    end
  end
end
