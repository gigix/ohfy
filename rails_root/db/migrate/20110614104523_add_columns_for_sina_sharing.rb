class AddColumnsForSinaSharing < ActiveRecord::Migration
  def self.up
    add_column :users, :sina_oauth_client_dump, :string
    add_column :plans, :share_to_sina, :boolean
  end

  def self.down
    remove_column :users, :sina_oauth_client_dump
    remove_column :plans, :share_to_sina
  end
end
