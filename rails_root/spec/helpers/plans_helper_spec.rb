require "#{File.dirname(__FILE__)}/../spec_helper"

describe PlansHelper do
  describe :delete_button do
    before(:each) do
      @user = create_user_with_plans
      @plan = @user.current_plan
    end
    
    it "generates button to destroy specified plan" do
      delete_button(@plan).should =~ /<form method="post" action="\/plans\/#{@plan.id}"  class="button_to">/
    end
    
    it "generates nothing if plan is not removable" do
      @plan.should_receive(:removable?).and_return(false)
      delete_button(@plan).should be_blank
    end
  end
end
