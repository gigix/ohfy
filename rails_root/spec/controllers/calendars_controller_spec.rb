require "#{File.dirname(__FILE__)}/../spec_helper"

describe CalendarsController do
  render_views
  
  describe :show do    
    it "redirects to signin page if user is not signed in" do
      get :show
      response.should be_redirect
    end
    
    describe 'with user signed in' do
      before(:each) do
        @user = create_test_user
        sign_in @user
      end
      
      it "renders if user signed in" do
        get :show
        response.should be_success
      end
    
      it "shows user name instead of email" do
        get :show
        response.body.should =~ /#{@user.name}/
        response.body.should_not =~ /#{@user.email}/
      end
    end
  end
end
