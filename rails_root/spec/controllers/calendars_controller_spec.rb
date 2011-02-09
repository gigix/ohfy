require "#{File.dirname(__FILE__)}/../spec_helper"

describe CalendarsController do
  render_views
  
  describe :show do
    it "redirects to signin page if user is not signed in" do
      get :show
      response.should be_redirect
    end
    
    it "renders if user signed in" do
      user = create_test_user
      sign_in user
      
      get :show
      response.should be_success
    end
  end
end
