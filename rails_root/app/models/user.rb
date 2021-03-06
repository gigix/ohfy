class User < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :token_authenticatable, :confirmable, :lockable and :timeoutable
  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :trackable, :validatable

  # Setup accessible (or protected) attributes for your model
  attr_accessible :email, :password, :password_confirmation, :remember_me, :sign_in_token
  
  has_many :plans, :include => {:executions => :activities}
  has_many :executions, :through => :plans, :include => :activities
  
  def self.sign_in!(email, password)
    user = find_for_authentication(:email => email)
    return nil unless user.valid_password?(password)

    sign_in_token = user.email + "|" + rand(Time.now.to_f).to_s
    user.update_attributes(:sign_in_token => sign_in_token)
    return sign_in_token
  end
  
  def name
    self.email.split('@').first
  end
  
  def create_plan!(from_date, habit_names, share_to_sina = false)    
    habit_names.reject!(&:blank?)
    raise if habit_names.blank?
    
    from_date = Date.parse(from_date) if(from_date.is_a?(String))
    
    plan = self.plans.build(:start_from => from_date, :share_to_sina => share_to_sina)

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
  
  def sina_oauth_client
    if(sina_oauth_client_dump.blank?)
      return OauthChina::Sina.new
    else
      return OauthChina::Sina.load(eval(sina_oauth_client_dump))
    end
  end
  
  def set_sina_oauth_client!(client)
    update_attribute(:sina_oauth_client_dump, client.dump.inspect)
  end
  
  def sina_ready?
    not sina_oauth_client_dump.blank?
  end
  
  def today
    time_zone.utc_to_local(Time.zone.now).to_date
  end
  
  def execution_on_today
    current_plan.execution_on_today
  end
  
  def execution_on_yesterday
    current_plan.execution_on(current_plan.today - 1)
  end
  
  def share_to_sina(date_str)
    return false unless sina_ready?
    return false unless current_plan
    return false unless current_plan.share_to_sina?
    sina_oauth_client.add_status(current_plan.execution_on(Date.parse(date_str)).share_message)
    return true
  end
  
  private
  def covered_by?(plan)
    plan.covers?(today)
  end
end
