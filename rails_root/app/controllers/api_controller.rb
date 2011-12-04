class ApiController < ApplicationController
  skip_before_filter :verify_authenticity_token
  
  SIGN_IN_TOKEN_NAME = "sign_in_token"
  
  def sign_in
    headers[SIGN_IN_TOKEN_NAME] = User.sign_in!(params[:email], params[:password])
    render :text => "Sign in succeeded"
  rescue
    render :text => "Sign in failed"
  end
  
  def todos
    sign_in_token = request.headers[SIGN_IN_TOKEN_NAME]
    raise "Invalid access" if sign_in_token.strip.blank?
    user = User.find_by_sign_in_token(sign_in_token)
    
    if request.get?
      execution = User.find_by_sign_in_token(sign_in_token).execution_on_today
      render :text => execution.to_json(:only => :id, :methods => :todo_items)
    else
      execution = User.find_by_sign_in_token(sign_in_token).executions.find(params[:execution_id])
      habit = execution.habits.find(params[:habit_id])
      done = (params[:done] == "true")
      
      done ? execution.act!(habit) : execution.deact!(habit)
      render :text => "Success"
    end
  end    
end
