require "#{File.dirname(__FILE__)}/../spec_helper"

describe ActivitiesController do
  before(:each) do
    @user = create_test_user
    sign_in @user
    
    @plan = @user.create_plan!(Date.today - 10, ['Gym', 'Drawing', 'Guitar'])
  end
  
  describe :toggle do
    it 'acts on given execution and habit' do
      execution = @plan.execution_on(Date.today)
      habit = @plan.habits.first
      
      lambda do
        post :toggle, :execution_id => execution, :habit_id => habit
        response.should be_success
      end.should change(Activity, :count).by(1)
      
      assigns[:execution].status_of(habit).should == 'acted'
    end
    
    it 'de-acts on given execution and habit if current status is acted' do
      execution = @plan.execution_on(Date.today)
      habit = @plan.habits.first
      execution.act!(habit)
      
      lambda do
        post :toggle, :execution_id => execution, :habit_id => habit
        response.should be_success
      end.should change(Activity, :count).by(-1)
      
      assigns[:execution].status_of(habit).should == 'not_acted'
    end
    
    it 'allows toggling executions does not belong to current plan' do
      execution = @plan.execution_on(Date.yesterday)
      @user.create_plan!(Date.today, ['Gym', 'Drawing', 'Guitar'])
      execution.plan.should_not == @user.current_plan
      
      lambda do
        post :toggle, :execution_id => execution, :habit_id => @plan.habits.first
        response.should be_success
      end.should change(Activity, :count).by(1)
    end
  end
end
