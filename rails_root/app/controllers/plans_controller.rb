class PlansController < ApplicationController
  before_filter :authenticate_user!

  def index
  end
  
  def new
    if(params[:clone])
      @plan = current_user.plans.find(params[:clone]).duplicate
    else
      @plan = current_user.plans.build(:start_from => current_user.today)
    end
  end
  
  def show
    @plan = current_user.plans.find(params[:id])
  rescue
    redirect_to plans_path
  end
  
  def create
    new_plan = current_user.create_plan!(params[:start_from], params[:habits], params[:share_to_sina])
    if(new_plan.share_to_sina? and current_user.sina_oauth_client_dump.blank?)
      oauth_client = current_user.sina_oauth_client
      authorize_url = oauth_client.authorize_url
      current_user.set_sina_oauth_client!(oauth_client)
      redirect_to authorize_url
    else
      redirect_to root_path
    end
  rescue
    flash[:alert] = "You need input at least ONE habit, as well as a valid start date (YYYY-mm-dd)."
    redirect_to new_plan_path
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
