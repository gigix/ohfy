class User < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :token_authenticatable, :confirmable, :lockable and :timeoutable
  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :trackable, :validatable

  # Setup accessible (or protected) attributes for your model
  attr_accessible :email, :password, :password_confirmation, :remember_me
  
  has_many :plans
  
  def create_plan!(for_date, habit_names)
    plan = self.plans.create!(:duration => for_date.strftime("%Y-%m"))
    habit_names.each do |name|
      plan.habits.create!(:title => name)
    end
  end
end
