require 'spec_helper'

describe PlansController do
  describe :create do
    it 'redirects to sign-in page if no user signed in' do
      post :create
      response.should redirect_to(new_user_session_path)
    end
    
    it 'creates plan' do
      user = create_test_user
      user.current_plan.should be_nil
      sign_in user
      
      lambda do
        post :create, :habits => ['Gym', 'Drawing', 'Guitar'], :start_from => Date.yesterday.to_s(:db)
        response.should redirect_to(root_path)
      end.should change(Plan, :count).by(1)

      user = User.find(user.id)
      user.current_plan.should_not be_nil
      user.current_plan.should have(3).habits
      user.current_plan.start_from.should == Date.yesterday
    end
  end
end
