require 'spec_helper'

describe CalendarsHelper do
  describe :draw_plan do
    it "generates calendar div" do
      plan = Plan.create(:start_from => Date.parse("2011-1-2"))
      result = helper.draw_plan(plan)
      result.should =~ /<div class=\"calendar\">/
      assert_date_count(30, result)
    end
    
    it "generates padding divs if plan does not start from sunday" do
      plan = Plan.create(:start_from => Date.parse("2011-1-20"))
      result = helper.draw_plan(plan)
      assert_date_count(34, result)
    end
    
    it "generates warning div if plan is nil" do
      result = helper.draw_plan(nil)
      result.should =~ /<div class=\"warning\">/
    end
  end
end

def assert_date_count(expected, result)
  result.split("<div class=\"date").size.should == expected + 1 + 7
end