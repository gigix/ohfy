class InsertTestData < ActiveRecord::Migration
  def self.up
    return unless RAILS_ENV == 'development'
    
    user = User.create!(:email => 'user@test.com', :password => 'password')
    user.create_plan!(Date.today, ['Gym', 'Drawing', 'Djembe'])
    
    User.create!(:email => 'empty.user@test.com', :password => 'password')
  end

  def self.down
  end
end
