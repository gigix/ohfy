require "#{File.dirname(__FILE__)}/../spec_helper"

describe Plan do
  before(:each) do
    @user = create_test_user
    @plan = @user.create_plan!((Date.today - 5), ['Drawing', 'Guitar', 'Gym'])
  end

  describe :execution_on do    
    it "returns execution on given date" do
      @plan.execution_on(Date.today).date.should == Date.today
    end
    
    it "returns nil if given date is out of range" do
      @plan.execution_on(Date.today - 10).should be_nil
    end
  end
  
  describe :removable? do
    it "returns true by default" do
      @plan.should be_removable
    end
    
    it "returns false if there are more than 3 acted executions" do
      (0..2).each{|i| @plan.execution_on(Date.today - i).act!(@plan.habits.first) }
      @plan.should_not be_removable
    end
  end
end
