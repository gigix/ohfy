require "#{File.dirname(__FILE__)}/../spec_helper"

describe CalendarsController do
  render_views
  
  describe :index do    
    it "redirects to signin page if user is not signed in" do
      get :index
      response.should be_redirect
    end
    
    describe 'with user signed in' do
      before(:each) do
        @user = create_test_user
        sign_in @user
      end
      
      it "renders" do
        get :index
        response.should be_success
      end
    
      it "shows user name instead of email" do
        get :index
        response.body.should =~ /#{@user.name}/
        response.body.should_not =~ /#{@user.email}/
      end
    end
  end
end
