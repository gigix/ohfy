require "#{File.dirname(__FILE__)}/../spec_helper"

describe User do
  before(:each) do
    @nick = create_test_user
  end
  
  describe :create_plan do
    it "creates a plan" do    
      from_date = Date.today
      lambda do
        @nick.create_plan!(from_date, ['Drawing', 'Guitar', 'Gym'])
      end.should change(Plan, :count).by(1)
    
      @nick.should have(1).plan
      plan = @nick.plans.first
      plan.start_from.should == from_date
      plan.should have(3).habits
    
      plan.should have(30).executions
      plan.executions.first.date.should == from_date
      plan.executions.last.date.should == from_date + 29.days
    end
  end
  
  describe :current_plan do
    it "returns nil if no plan covers today" do
      @nick.current_plan.should be_nil
      
      @nick.create_plan!(Date.today - 2.months, ['Gym'])
      @nick.current_plan.should be_nil
    end
    
    it "returns current plan" do
      plan = @nick.create_plan!(Date.today - 2.days, ['Gym'])
      @nick.current_plan.should == plan
    end
  end
end