class Post < ActiveRecord::Base
	atrre_accessible :address, :email, :name
end
