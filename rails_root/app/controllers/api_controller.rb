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
    user = sign_in_user
    
    if request.get?
      execution = user.execution_on_today
      render :text => execution.to_json(:only => :id, :methods => :todo_items)
    else
      execution = user.executions.find(params[:execution_id])
      habit = execution.habits.find(params[:habit_id])
      done = (params[:done] == "true")
      
      done ? execution.act!(habit) : execution.deact!(habit)
      render :text => "Success"
    end
  end    
  
  def plan
    sign_in_user.create_plan!(sign_in_user.today, params[:habit_names])
    render :text => "Success"
  end
  
  private
  def sign_in_user
    sign_in_token = request.headers[SIGN_IN_TOKEN_NAME]
    raise "Invalid access" if sign_in_token.strip.blank?
    User.find_by_sign_in_token(sign_in_token)
  end
end
