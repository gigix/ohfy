require "#{File.dirname(__FILE__)}/../spec_helper"

describe Plan do
  describe :execution_on do
    before(:each) do
      @user = create_test_user
      @plan = @user.create_plan!((Date.today - 3), ['Drawing', 'Guitar', 'Gym'])
    end
    
    it "returns execution on given date" do
      @plan.execution_on(Date.today).date.should == Date.today
    end
    
    it "returns nil if given date is out of range" do
      @plan.execution_on(Date.today - 10).should be_nil
    end
  end
end
