require 'spec_helper'

describe User do
  it "can create a plan" do
    user = User.create!(:email => 'user@test.com', :password => 'P@55w0rd')
    
    lambda do
      user.create_plan!(DateTime.parse('2011-1-23'), ['Drawing', 'Guitar', 'Gym'])
    end.should change(Plan, :count).by(1)
    
    user.should have(1).plan
    plan = user.plans.first
    plan.duration.should == "2011-01"
    plan.should have(3).habits
  end
end
