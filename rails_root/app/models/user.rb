class User < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :token_authenticatable, :confirmable, :lockable and :timeoutable
  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :trackable, :validatable

  # Setup accessible (or protected) attributes for your model
  attr_accessible :email, :password, :password_confirmation, :remember_me
  
  has_many :plans
  
  def name
    self.email.split('@').first
  end
  
  def create_plan!(from_date, habit_names)    
    habit_names.reject!(&:blank?)
    raise if habit_names.blank?
    
    from_date = Date.parse(from_date) if(from_date.is_a?(String))
    
    plan = self.plans.build(:start_from => from_date)

    self.active_plans.each {|plan| plan.abandon!} if plan.covers?(Date.today)

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
      plan.covers?(Date.today)
    end
  end  
end
