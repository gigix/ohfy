require 'spec_helper'

describe ApiController do
  before(:each) do
    @user = create_user_with_plans
  end
  
  describe :sign_in do
    it "returns unique token for valid sign in" do
      post :sign_in, :email => @user.email, :password => @user.password
      sign_in_token = response.header["sign_in_token"]
      sign_in_token.should_not be_nil
      User.find_by_sign_in_token(sign_in_token).should == @user
    end
    
    it "does not return token for invalid sign in" do
      post :sign_in, :email => @user.email, :password => "not.this.password"
      response.header[ApiController::SIGN_IN_TOKEN_NAME].should be_nil
    end
    
    it "does not return token for email not exist" do
      post :sign_in, :email => "does.not.exist@user.com", :password => "not.this.password"
      response.header[ApiController::SIGN_IN_TOKEN_NAME].should be_nil
    end
  end
  
  describe :todos do
    before(:each) do
      post :sign_in, :email => @user.email, :password => @user.password
      sign_in_token = response.header[ApiController::SIGN_IN_TOKEN_NAME]
      request.env[ApiController::SIGN_IN_TOKEN_NAME] = sign_in_token
    end
    
    it "returns activities of today" do
      get :todos
      response.body.should be_include("Swimming")
      response.body.should be_include("Drawing")
    end
    
    it "updates execution status" do
      execution = @user.execution_on_today
      habit = execution.habits.first
      execution.should_not be_acted(habit)
      
      post :todos, :execution_id => execution.id, :habit_id => habit.id, :done => "true"
      response.should be_success
      
      execution.reload
      execution.should be_acted(habit)
    end
    
    it "fails without sign in token" do
      request.env[ApiController::SIGN_IN_TOKEN_NAME] = " "
      lambda{ get :todos }.should raise_error
    end
  end
end
