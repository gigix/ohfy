require "#{File.dirname(__FILE__)}/../spec_helper"

describe CalendarsHelper do
  describe :draw_plan do
    before(:each) do
      @user = create_test_user
    end
    
    it "generates calendar div" do
      plan = @user.plans.create!(:start_from => Date.parse("2011-1-2"))
      result = helper.draw_plan(plan)
      result.should =~ /<div class='ohfy_calendar'>/
      assert_date_count(30, result)
    end
    
    it "generates padding divs if plan does not start from sunday" do
      plan = @user.plans.create!(:start_from => Date.parse("2011-1-20"))
      result = helper.draw_plan(plan)
      assert_date_count(34, result)
    end
  end
end

def assert_date_count(expected, result)
  result.split("<div class='date").size.should == expected + 1 + 7
end