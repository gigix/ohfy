class PlansController < ApplicationController
  before_filter :authenticate_user!

  def index
  end
  
  def show
    @plan = current_user.plans.find(params[:id])
  rescue
    redirect_to plans_path
  end
  
  def create
    current_user.create_plan!(params[:start_from], params[:habits])
    redirect_to root_path
  rescue
    flash[:alert] = "You need input at least ONE habit, as well as a valid start date (YYYY-mm-dd)."
    redirect_to root_path
  end
  
  def destroy
    begin
      plan = current_user.plans.find(params[:id])
      plan.destroy if plan.removable?
    rescue
    end
    
    redirect_to plans_path
  end
end
