require 'spec_helper'

describe CalendarsController do
  describe :show do
    it "redirects to signin page if user is not signed in" do
      get :show
      response.should be_redirect
    end
    
    it "renders if user signed in" do
      user = User.create!(:email => 'user@test.com', :password => 'P@55w0rd')
      sign_in user
      
      get :show
      response.should be_success
    end
  end
end
