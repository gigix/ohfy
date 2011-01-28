require 'spec_helper'

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
      
      execution.status_of(habit).should == 'acted'
    end
    
    it 'de-acts on given execution and habit if current status is acted' do
      execution = @plan.execution_on(Date.today)
      habit = @plan.habits.first
      execution.act!(habit)
      
      lambda do
        post :toggle, :execution_id => execution, :habit_id => habit
        response.should be_success
      end.should change(Activity, :count).by(-1)
      
      execution.status_of(habit).should == 'not_acted'
    end
  end
end
