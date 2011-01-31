class WidgetsController < ApplicationController
  def show
    @user = User.find(params[:id])
    render :layout => false
  end
end
