package platformer.code.gamelogic.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import platformer.code.gameengine.PhysicsObject;
import platformer.code.gameengine.graphics.MyGraphics;
import platformer.code.gameengine.hitbox.RectHitbox;
import platformer.code.gamelogic.Main;
import platformer.code.gamelogic.level.Level;
import platformer.code.gamelogic.tiles.Tile;

public class Player extends PhysicsObject{
	private float walkSpeed = 600;
	private int walkChange = 100;
	private int slowMuffin = 10;
	private float accSpeedX = 0;
	private float jumpPower = 1350;
	private int damage = 0;
	private boolean dashing = false;
	private int stocksLeft = 1;
	private boolean damaged = false;
	private int maxJumps = 0;
	private int jumpsTaken = 0;
	private int wallJumpMax = 0;
	private int wallJumpsTaken = 0;
	private long wallHoldTimer = 0;
	private long timeOnWallMax = 0;
	private long damagedTimer = 0;
	private double damagedTime = 0;
	protected BufferedImage image;
	private boolean isJumping = false;

	public Player(float x, float y, Level level) {
	
		super(x, y, level.getLevelData().getTileSize(), level.getLevelData().getTileSize(), level);
		int offset =(int)(level.getLevelData().getTileSize()*0.1); //hitbox is offset by 10% of the player size.
		this.hitbox = new RectHitbox(this, offset + (int)(.2*width),offset, width -(offset*4), height - offset);
	}

	@Override
	public void update(float tslf) {
		super.update(tslf);
		if(damaged && (System.currentTimeMillis()-damagedTimer)/1000>damagedTime) {
			damaged = false;
		}
		movementVector.x = 0;
		if(PlayerInput.isLeftKeyDown()) {
			if(accSpeedX < -walkSpeed && !dashing && !damaged) {
				accSpeedX += slowMuffin;
				if(accSpeedX > -walkSpeed) {
					accSpeedX = -walkSpeed;
				}
			}
			else if (accSpeedX > -walkSpeed && !dashing && !damaged) {
				accSpeedX -= walkChange;
				if(accSpeedX < -walkSpeed) {
					accSpeedX = -walkSpeed;
				}
			}
			else if(!damaged && !dashing){
				accSpeedX = -walkSpeed;
			}
		}
		if(PlayerInput.isRightKeyDown()) {
			if(accSpeedX > walkSpeed && !dashing && !damaged) {
				accSpeedX -= slowMuffin;
				if(accSpeedX < walkSpeed) {
					accSpeedX = walkSpeed;
				}
			}
			else if (accSpeedX < walkSpeed && !dashing && !damaged) {
				accSpeedX += walkChange;
				if(accSpeedX > walkSpeed) {
					accSpeedX = walkSpeed;
				}
			}
		}
		if(!PlayerInput.isRightKeyDown() && !PlayerInput.isLeftKeyDown() && !dashing) {
			if(accSpeedX >= -100 && accSpeedX <= 100) {
				accSpeedX = 0;
			}
			else if(!damaged){
				accSpeedX -= (int)(accSpeedX/2);
			}
			else {
				accSpeedX -= (int)(accSpeedX/4);
			}
		}
		movementVector.x = accSpeedX;
		if(PlayerInput.isJumpKeyDown() && !isJumping) {
			movementVector.y = -jumpPower;
			isJumping = true;
		}
		
		isJumping = true;
		if(collisionMatrix[BOT] != null) isJumping = false;
	}

	public void launchedChar(double time, int launch) {
		damagedTime = time;
		damagedTimer = System.currentTimeMillis();
		damaged = true;
		accSpeedX = launch;
	}

	public boolean liveThis() {
		if(damage >= 100) {
			stocksLeft--;
			damage = 0;
		}
		if(stocksLeft <= 0) {
			return false;
		}
		return true;
	}

	public void damageChar(int damageTaken) {
		damage+=damageTaken;
		if(damage < 0) {
			damage = 0;
		}
	}


	public void launchChar(int launch, int direction, int damageTaken, double time) {
		if(!damaged) {
			damagedTime = time;
			damagedTimer = System.currentTimeMillis();
			damaged = true;
		}
		if(direction == 0) {
			movementVector.x = 0;
			accSpeedX = 0;
			movementVector.y = (float)(-1*(1+.01*damage)*launch);
			damage+=damageTaken;
		}
		else if(direction == 1) {
			movementVector.x = (float)(-1*(1+(.01*damage))*launch/2);
			accSpeedX = (float)(-1*(1+(.01*damage))*launch/2);
			movementVector.y = (float)(-1*(1+.01*damage)*launch/2);
			damage+=damageTaken;
		}
		else if(direction == 2) {
			movementVector.x = (float)(-1*(1+(.01*damage))*launch);
			accSpeedX = (float)(-1*(1+(.01*damage))*launch);
			movementVector.y = 0;
			damage+=damageTaken;
		}
		else if(direction == 3) {
			movementVector.x = (float)(-1*(1+(.01*damage))*launch/2);
			accSpeedX = (float)(-1*(1+(.01*damage))*launch/2);
			movementVector.y = (float)((1+.01*damage)*launch/2);
			damage+=damageTaken;
		}
		else if(direction == 4) {
			movementVector.x = 0;
			accSpeedX = 0;
			movementVector.y = (float)((1+.01*damage)*launch);
			damage+=damageTaken;
		}
		else if(direction == 5) {
			movementVector.x = (float)((1+(.01*damage))*launch/2);
			accSpeedX = (float)((1+(.01*damage))*launch/2);
			movementVector.y = (float)((1+.01*damage)*launch/2);
			damage+=damageTaken;
		}
		else if(direction == 6) {
			movementVector.x = (float)((1+.01*damage)*launch);
			accSpeedX = (float)((1+.01*damage)*launch);
			movementVector.y = 0;
			damage+=damageTaken;
		}
		else if(direction == 7) {
			movementVector.x = (float)((1+.01*damage)*launch/2);
			accSpeedX = (float)((1+.01*damage)*launch/2);;
			movementVector.y = (float)(-1*(1+.01*damage)*launch/2);
			damage+=damageTaken;
		}
		System.out.println("launched");
	}

	public void setPlayerImage (BufferedImage m) {
		image = m;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(image, (int)position.x, (int)position.y, width, height, null);
		
		if(Main.DEBUGGING) {
			for (int i = 0; i < closestMatrix.length; i++) {
				Tile t = closestMatrix[i];
				if(t != null) {
					g.setColor(Color.RED);
					g.drawRect((int)t.getX(), (int)t.getY(), t.getSize(), t.getSize());
				}
			}
		}
		
		hitbox.draw(g);
	}
}
