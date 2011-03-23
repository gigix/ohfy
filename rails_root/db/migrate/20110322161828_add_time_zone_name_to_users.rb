class AddTimeZoneNameToUsers < ActiveRecord::Migration
  def self.up
    add_column :users, :time_zone_name, :string, :default => 'UTC'    
    User.connection.execute("UPDATE users SET time_zone_name = 'Beijing'")
  end

  def self.down
    remove_column :users, :time_zone_name
  end
end
