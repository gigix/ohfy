require "#{File.dirname(__FILE__)}/../spec_helper"

describe User do
  before(:each) do
    @nick = create_test_user
    @nick.update_attribute(:time_zone_name, 'Beijing')
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
    
    it "does not change current plan unless new plan covers today" do
      current_plan = @nick.create_plan!(@nick.today - 1, ['Drawing', 'Guitar', 'Gym'])
      future_plan = @nick.create_plan!(@nick.today + 1, ['Swimming'])
      
      @nick.current_plan.should == current_plan
    end
  end
  
  describe :current_plan do
    it "returns nil if no plan covers today" do
      @nick.current_plan.should be_nil
      
      @nick.create_plan!(@nick.today - 2.months, ['Gym'])
      @nick.current_plan.should be_nil
      
      @nick.create_plan!(@nick.today + 1.day, ['Gym'])
      @nick.current_plan.should be_nil
    end
    
    it "returns current plan" do
      plan = @nick.create_plan!(@nick.today - 2.days, ['Gym'])
      @nick.current_plan.should == plan
    end
    
    it "returns current plan if this plan starts from today" do
      plan = @nick.create_plan!(@nick.today, ['Gym'])
      @nick.current_plan.should == plan
    end
  end
  
  describe :time_zone do
    it "returns time zone based on time_zone_name" do
      @nick.time_zone.should == ActiveSupport::TimeZone.new(8)
    end    
  end
  
  describe :execution_on_today do
    it "returns execution on 'today' based on time zone" do
      @nick.update_attribute(:time_zone_name, 'Beijing')
      @nick.create_plan!(Date.yesterday, ['Gym'])
      @nick.execution_on_today.date.should == @nick.today
    end
  end
  
  describe :sina_oauth_client do
    # TODO: should find a place where can always access internet to run these tests
    
    it "returns new sina oauth client if there's no one exists"
    #   @nick.sina_oauth_client_dump.should be_nil
    #   @nick.sina_oauth_client.should_not be_nil
    # end

    it "returns sina oauth client loaded from database"
    #   client = OauthChina::Sina.new
    #   @nick.set_sina_oauth_client!(client)
    #   @nick.sina_oauth_client.dump.should == client.dump
    # end
  end
  
  describe :share_to_sina do
    it "does nothing if user is not sina ready" do
      @nick.share_to_sina(Date.today.to_s(:db)).should be_false
    end
    
    it "does nothing if user doesn't have a current plan" do
      @nick.should_receive(:sina_ready?).and_return(true)
      @nick.share_to_sina(Date.today.to_s(:db)).should be_false
    end
    
    it "does nothing if current plan cannot share to sina" do
      @nick.create_plan!(Date.yesterday, ["Gym"])
      @nick.should_receive(:sina_ready?).and_return(true)
      @nick.share_to_sina(Date.today.to_s(:db)).should be_false
    end
    
    it "shares execution of given date to sina" do
      current_plan = @nick.create_plan!(Date.yesterday, ["Gym"])
      current_plan.update_attribute(:share_to_sina, true)
      @nick.should_receive(:sina_ready?).and_return(true)
      
      mock_client = Object.new
      @nick.should_receive(:sina_oauth_client).and_return(mock_client)
      mock_client.should_receive(:add_status)
      
      @nick.share_to_sina(Date.today.to_s(:db)).should be_true
    end
  end
end