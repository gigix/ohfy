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
    
    it 'uses short_title instead of full title' do
      habit = @plan.habits.first
      habit.update_attribute(:title, 'abcdefghijklmnopqrstuvwxyz')
      habit.summary.should == 'abcdefghijklmnopqrst... (0 day)'
    end
  end
  
  describe :short_title do
    it 'cuts title down to 20 characters' do
      habit = Habit.new(:title => 'abcdefghijklmnopqrstuvwxyz')
      habit.short_title.should == 'abcdefghijklmnopqrst...'
    end
  end
end
