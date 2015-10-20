class WelcomeController < ApplicationController
  	def index
		@users = User.all
		respond_to do |format|
			format.html
			format.json {render json: @users }
		end

	end
	def costumer
		@items = Item.all
		respond_to do |format|
			format.html
			format.json {render json: @items }
		end

  	end
	def show
		@item = Item.find(params[:id])

		respond_to do |format|
			format.html
			format.json {render json: @items }
		end

	end
end
