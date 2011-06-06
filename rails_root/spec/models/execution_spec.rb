require "#{File.dirname(__FILE__)}/../spec_helper"

describe Execution do
  before(:each) do
    @user = create_test_user
    @plan = @user.create_plan!((Date.today - 3), ['Drawing', 'Guitar', 'Gym'])
  end
  
  describe :description do
    it "returns a readable description of today's execution" do
      date = Date.today - 3
      execution = @plan.execution_on(date)
      
      execution.description.should == "#{date.to_s(:db)}: did nothing."
      
      execution.act!(execution.habits.first)
      execution.description.should == "#{date.to_s(:db)}: Drawing."
      
      execution.act!(execution.habits.second)
      execution.description.should == "#{date.to_s(:db)}: Drawing; Guitar."
      
      execution.act!(execution.habits.last)
      execution.description.should == "#{date.to_s(:db)}: Drawing; Guitar; Gym."
    end
  end
  
  describe :status do
    it "returns GREAT if all habits are acted" do
      date = Date.today - 3
      execution = @plan.execution_on(date)
      
      lambda do
        execution.habits.each{|habit| execution.act!(habit) }
      end.should change(Activity, :count).by(3)
      
      execution.status.should == Execution::Status::GREAT
    end
    
    it "returns GOOD if some habits are acted" do
      execution = @plan.execution_on(Date.today - 2)
      execution.act!(execution.habits.first)
      execution.status.should == Execution::Status::GOOD
    end
    
    it "returns BAD if no habit is acted" do
      @plan.execution_on(Date.today - 1).status.should == Execution::Status::BAD
    end
    
    it "returns UNKNOWN for future dates" do
      @plan.execution_on(Date.tomorrow).status.should == Execution::Status::UNKNOWN
    end
    
    it "returns UNKNOWN if no habit is acted for today" do
      execution = @plan.execution_on(Date.today)
      execution.status.should == Execution::Status::UNKNOWN
      
      execution.act!(execution.habits.first)
      execution.status.should == Execution::Status::GOOD
    end
  end
end
