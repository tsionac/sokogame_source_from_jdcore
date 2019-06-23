package Data;

public class Cell {

	public final int _x,_y;
	private Boolean _isFloor;
	private Boolean _isStorage;
	private Boolean _hasPlayer;
	private Boolean _hasBox;

	
	/**
	 * construct a Wall cell
	 */
	public Cell(int x,int y) {
		_isFloor = false;
		_isStorage = false;
		_hasPlayer = false;
		_hasBox = false;
		_x = x;
		_y = y;
	}
	
	/**
	 * construct a Floor cell
	 */
	public Cell(int x,int y, Boolean isStorage, Boolean hasPlayer, Boolean hasBox) {
		_isFloor = true;
		_isStorage = isStorage;
		_hasPlayer = hasPlayer;
		_hasBox = hasBox;
		_x = x;
		_y = y;
	}
	
	public Boolean isWall(){
		return !_isStorage & !_hasPlayer & !_hasBox & !_isFloor;
	}
	
	public Boolean isFloor() {
		return _isFloor;
	}
	public Boolean isStorage() {
		return _isStorage;
	}
	public Boolean hasPlayer() {
		return _hasPlayer;
	}
	public Boolean hasBox() {
		return _hasBox;
	}
	
	public void set_isStorage(Boolean _isStorage) {
		if (!_isFloor) return;
		this._isStorage = _isStorage;
	}
	public void set_hasPlayer(Boolean _hasPlayer) {
		if (!_isFloor) return;
		this._hasPlayer = _hasPlayer;
	}
	public void set_hasBox(Boolean _hasBox) {
		if (!_isFloor) return;
		this._hasBox = _hasBox;
	}

	public boolean isEmptyFloor() {
		return isFloor() && !hasBox() && !hasPlayer();
	}
	
	@Override
	public Cell clone() {
		Cell c = new Cell(_x, _y);
		c._hasBox = this._hasBox;
		c._hasPlayer = this._hasPlayer;
		c._isFloor = this._isFloor;
		c._isStorage = this._isStorage;
		return c;
	}
	
}
