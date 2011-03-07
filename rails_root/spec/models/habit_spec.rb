require "#{File.dirname(__FILE__)}/../spec_helper"

describe Habit do
  describe :summary do
    before(:each) do
      @user = create_test_user
      @plan = @user.create_plan!((Date.today - 10), ['Drawing', 'Guitar', 'Gym'])
    end
    
    it 'includes title and number of executed days' do
      @plan.habits.each{|habit| habit.summary.should == "#{habit.title} (0 day)"}
      
      habit = @plan.habits.first
      @plan.execution_on(Date.yesterday).act!(habit)
      habit.reload.summary.should == "#{habit.title} (1 day)"
      
      @plan.execution_on(Date.today).act!(habit)
      habit.reload.summary.should == "#{habit.title} (2 days)"
    end
  end
end
