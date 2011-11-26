class ApiController < ApplicationController
  skip_before_filter :verify_authenticity_token
  
  def sign_in
    user = User.find_for_authentication(:email => params[:email])
    if user and user.valid_password?(params[:password])
      headers["sign_in_token"] = user.email 
      render :text => "Sign in succeeded"
    else
      render :text => "Sign in failed"
    end
  end
end
