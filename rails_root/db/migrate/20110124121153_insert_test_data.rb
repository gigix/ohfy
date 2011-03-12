class InsertTestData < ActiveRecord::Migration
  def self.up
    return if RAILS_ENV == 'production'
    
    user = User.create!(:email => 'user@test.com', :password => 'password')
    user.create_plan!((Date.today - 10), ['Gym', 'Drawing', 
      'I want to do a lot of cool things but I donnot have enough time so I have to sleep a little bit later everyday'])
    
    User.create!(:email => 'empty.user@test.com', :password => 'password')
  end

  def self.down
  end
end
