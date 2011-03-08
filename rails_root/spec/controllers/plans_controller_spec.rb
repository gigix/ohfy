require "#{File.dirname(__FILE__)}/../spec_helper"

describe PlansController do
  render_views
  
  before(:each) do
    @user = create_user_with_plans
  end
  
  describe :index do
    it 'redirects to sign-in page if no user signed in' do
      get :index
      response.should redirect_to(new_user_session_path)
    end
    
    it 'renders with all plans of current user' do
      user = create_user_with_plans
      sign_in user
      
      get :index
      response.should be_success
    end
  end
  
  describe :destroy do
    before(:each) do
      sign_in @user
    end
    
    it 'destroys specified plan and redirects to plans list page' do
      lambda do
        post :destroy, :id => @user.plans.first
        response.should redirect_to(plans_path)
      end.should change(Plan, :count).by(-1)
    end
    
    it 'also destroys executions belong to specified plan' do
      lambda do
        post :destroy, :id => @user.plans.first
      end.should change(Execution, :count).by(-30)
    end
    
    it 'does not destroy plan not belong to current user' do
      another_user = create_user_with_plans
      lambda do
        post :destroy, :id => another_user.plans.first
        response.should redirect_to(plans_path)
      end.should_not change(Plan, :count)
    end
    
    it 'does not destroy plan which is not removable' do
      plan = @user.current_plan
      lambda do 
        (0..2).each{|i| plan.executions[i].act!(plan.habits.first) }
      end.should change(Activity, :count).by(3)
      
      lambda do
        post :destroy, :id => plan
        response.should redirect_to(plans_path)
      end.should_not change(Plan, :count)
    end
  end
  
  describe :create do
    it 'redirects to sign-in page if no user signed in' do
      post :create
      response.should redirect_to(new_user_session_path)
    end
    
    it 'creates plan' do
      @user.current_plan.destroy
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
    
    it 'does not create habit with empty name' do
      sign_in @user
      
      lambda do
        post :create, :habits => ['Gym', '', '  '], :start_from => Date.yesterday.to_s(:db)
        response.should redirect_to(root_path)
      end.should change(Habit, :count).by(1)
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
