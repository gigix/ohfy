class PlansController < ApplicationController
  before_filter :authenticate_user!
  
  def create
    current_user.create_plan!(params[:start_from], params[:habits])
    redirect_to root_path
  end
end
