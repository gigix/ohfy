require 'spec_helper'

describe ApiController do
  before(:each) do
    @user = create_test_user
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
      response.header["sign_in_token"].should be_nil
    end
    
    it "does not return token for email not exist" do
      post :sign_in, :email => "does.not.exist@user.com", :password => "not.this.password"
      response.header["sign_in_token"].should be_nil
    end
  end
  
  describe :todos do
    before(:each) do
      post :sign_in, :email => @user.email, :password => @user.password
      sign_in_token = response.header["sign_in_token"]      
      request.headers["sign_in_token"] = sign_in_token
    end
    
    it "returns activities of today" do
      get :todos
      response.body.should be_include("Android")
    end
    
    it "updates execution status"
  end
end
