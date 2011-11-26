require 'spec_helper'

describe ApiController do
  before(:each) do
    @user = create_test_user
  end
  
  describe :sign_in do
    it "returns unique token for valid sign in" do
      post :sign_in, :email => @user.email, :password => @user.password
      response.header["sign_in_token"].should_not be_nil
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
end
