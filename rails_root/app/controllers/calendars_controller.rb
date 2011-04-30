class CalendarsController < ApplicationController
  before_filter :authenticate_user!
  
  def index
    redirect_to(new_plan_path) unless current_user.current_plan
  end  
end
