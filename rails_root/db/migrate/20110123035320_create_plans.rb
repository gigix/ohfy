class CreatePlans < ActiveRecord::Migration
  def self.up
    create_table :plans do |t|
      t.integer :user_id
      
      t.date :start_from
      t.string :status, :default => Plan::Status::ACTIVE
      
      t.boolean :share_to_sina
      
      t.timestamps
    end
  end

  def self.down
    drop_table :plans
  end
end
