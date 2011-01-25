require 'spec_helper'

describe PlansController do
  before(:each) do
    @user = create_test_user
  end
  
  describe :create do
    it 'redirects to sign-in page if no user signed in' do
      post :create
      response.should redirect_to(new_user_session_path)
    end
    
    it 'creates plan' do
      @user.current_plan.should be_nil
      sign_in @user
      
      lambda do
        post :create, :habits => ['Gym', 'Drawing', 'Guitar'], :start_from => Date.yesterday.to_s(:db)
        response.should redirect_to(root_path)
      end.should change(Plan, :count).by(1)

      user = User.find(@user.id)
      user.current_plan.should_not be_nil
      user.current_plan.should have(3).habits
      user.current_plan.start_from.should == Date.yesterday
    end
    
    it 'redirects back with error message if no habit inputted' do
      sign_in @user
      lambda do
        post :create, :habits => [], :start_from => Date.yesterday.to_s(:db)
        response.should redirect_to(root_path)
        flash[:alert].should_not be_blank
      end.should_not change(Plan, :count)
    end
    
    it 'redirects back with error message if start date is invalid' do
      sign_in @user
      lambda do
        post :create, :habits => ['Gym'], :start_from => 'blah blah'
        response.should redirect_to(root_path)
        flash[:alert].should_not be_blank
      end.should_not change(Plan, :count)
    end
  end
end
