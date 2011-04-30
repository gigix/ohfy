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
  
  describe :duplicate do
    it "creates a new unsaved plan with same habits and starts from today" do
      lambda do
        new_plan = @plan.duplicate
        new_plan.habits.should == @plan.habits
        new_plan.start_from.should_not == @plan.start_from
        new_plan.start_from.should == @user.today
      end.should_not change(Plan, :count)
    end
  end
end
