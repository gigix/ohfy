require "#{File.dirname(__FILE__)}/../spec_helper"

describe PlansHelper do
  describe :delete_button do
    it "generates button to destroy specified plan"
    it "generates nothing if plan is not removable"
  end
end
