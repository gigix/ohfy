class CreateActivities < ActiveRecord::Migration
  def self.up
    create_table :activities do |t|
      t.integer :execution_id
      t.integer :habit_id
      
      t.string :status

      t.timestamps
    end
  end

  def self.down
    drop_table :activities
  end
end
