class WidgetsController < ApplicationController
  def show
    @user = User.find(params[:id])
    render :layout => false
  end
  
  def css
    render :layout => false, :content_type => 'text/css'
  end
end
