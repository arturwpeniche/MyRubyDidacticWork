class CreateItems < ActiveRecord::Migration
  def change
    create_table :items do |t|
      t.string :name
      t.string :brand
      t.string :tipo
      t.integer :stock
      t.float :price

      t.timestamps
    end
  end
end
