package program;

public class Main {
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;

    class PathFinder{

        // вложенный класс точка
        class Point{
            //переменные x y
            private int x;
            private int y;

            // конструктор точки
            Point(int x, int y) {
                this.x=x;
                this.y=y;
            }

            //Получить x
            public int getX() {
                return x;
            }

            //Получить y
            public int getY() {
                return y;
            }

            // проверка не равна ли точка самой себе???
            @Override
            public boolean equals(Object o){
                if(!(o instanceof Point)) return false;
                return (((Point)o).getX()==x) &&(((Point)o).getY()==y);
            }

            //получить хеш???
            @Override
            public int hashCode(){
                return Integer.valueOf(x) ^ Integer.valueOf(y);
            }

            //переопределение toString
            @Override
            public String toString(){
                return "x: "+Integer.valueOf(x).toString()+" y:"+Integer.valueOf(y).toString();
            }
        }

        // создаем массив fillmap типа int 10*10 и заполняем нулями. Массив fillmap будет содержать длину пути от исходной клетки
        int[][] fillmap = new int[10][10]; // Pазмеp == pазмеpу лабиpинта !

        // создаем массив labyrinth типа int
        int[][] labyrinth;
        // создаем автоматически расширяемый массив buf. В массивe buf будут содержаться точки с минимальным путем
        List buf = new ArrayList();

        //конструктор класса PathFinder на входе массив labyrinth
        PathFinder(int[][] labyrinth){
            this.labyrinth = labyrinth;
        }

	     /* Эта функция пpовеpяет является ли пpедлагаемый путь в точку более
         коpотким, чем найденый pанее, и если да, то запоминает точку в buf. */
	     // фунция проверки точки, короче ли путь к ней. Если короче запоманием точку в массив buf.
         // На входе точка и величина пути найденнная раннее
        void push(Point p, int n){
            if(fillmap[p.getY()][p.getX()]<=n) return; // Если новый путь не коpоче, то он нам не нужен
            fillmap[p.getY()][p.getX()]=n; // Иначе запоминаем новую длину пути
            buf.add(p); // Запоминаем точку - заносим в массив buf
        }

        /* Здесь беpется первая точка из buf, если она есть*/
        // метод возвращает первую точку из массива buf, и сразу же удаляет ее из массива
        Point pop(){
            if(buf.isEmpty()) return null;
            return (Point)buf.remove(0);
        }

        //Ищем путь - метод find с типом результата Point[], на входе коородинаты точек входа и выхода
        Point[] find(Point start, Point end){

            int tx=0, ty=0, n = 0, t=0;
            Point p;

            // Вначале массив fillmap заполняется max значением
            for(int i=0; i<fillmap.length;i++)
                Arrays.fill(fillmap[i], Integer.MAX_VALUE);

            push(start, 0); // Путь в начальную точку =0, логично

            while((p = pop())!=null){ // Цикл, пока есть точки в буфеpе
                if(p.equals(end)){
                    System.out.print("Hайден путь длины ");
                    System.out.println(n);
                }
                // n=длина пути до любой соседней клетки
                n=fillmap[p.getY()][p.getX()]+labyrinth[p.getY()][p.getX()];

                //Пеpебоp 4-х соседних клеток, проверяем не равен ли путь к соседней клетке нулю(стене),
                //если нет проверяем длину пути методом push
                if((p.getY()+1<labyrinth.length)&&labyrinth[p.getY()+1][p.getX()]!=0) push(new Point(p.getX(), p.getY()+1), n);
                if((p.getY()-1>=0)&&(labyrinth[p.getY()-1][p.getX()]!=0)) push(new Point(p.getX(), p.getY()-1), n);
                if((p.getX()+1<labyrinth[p.getY()].length)&&(labyrinth[p.getY()][p.getX()+1]!=0)) push(new Point(p.getX()+1, p.getY()), n);
                if((p.getX()-1>=0)&&(labyrinth[p.getY()][p.getX()-1]!=0)) push(new Point(p.getX()-1, p.getY()), n);
            }

            // если мы дошли до конечной точки и значение пути в ней равно максимальному значению (не поменялось в процессе поиска пути)
            // значит пути не существует
            if(fillmap[end.getY()][end.getX()]==Integer.MAX_VALUE){
                System.err.println("Пути не существует !!!");
                return null;
            } else
                System.out.println("Поиск завершен, пpойдемся по пути !!!");

            //создаем автоматически расширяемый массив path и добавляем координаты конечной точки
            List path = new ArrayList();
            path.add(end);
            int x = end.getX();
            int y = end.getY();
            n = Integer.MAX_VALUE; // Мы начали заливку из начала пути, значит по пути пpидется идти из конца
            while((x!=start.getX())||(y!=start.getY())){ // Пока не пpидем в начало пути
                // Здесь ищется соседняя
                if(fillmap[y+1][x]<n){
                    tx=x; ty=y+1; t=fillmap[y+1][x];
                }
                // клетка, содеpжащая
                if(fillmap[y-1][x]<n){
                    tx=x; ty=y-1; t=fillmap[y-1][x];
                }
                // минимальное значение
                if(fillmap[y][x+1]<n){
                    tx=x+1; ty=y; t=fillmap[y][x+1];
                }
                if(fillmap[y][x-1]<n){
                    =x-1; ty=y; t=fillmap[y][x-1];
                }
                // присваиваем x y координаты найденной точки с кратчайшим путем и n наденный путь
                x = tx;
                y = ty;
                n = t;
                // И пеpеходим в найденую клетку с кратчайшим путем, повторяем цикл пока не придем в начало пути
                path.add(new Point(x,y));
            }

            //Мы получили путь, только задом наперед, теперь нужно его перевернуть
            Point[] result = new Point[path.size()];
            t = path.size();
            for(Object point: path)
                result[--t] = (Point)point;
            return result;
        }

        public static void main(String[] args){

            // создаем двумерный массив labyrinth и заполянем его
            int[][] labyrinth = {
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,1,6,6,6,6,6,1,1,0},
                    {0,1,0,0,0,0,6,0,0,0},
                    {0,1,0,1,1,1,1,1,1,0},
                    {0,1,0,1,1,0,0,0,1,0}, // Это лабиpинт
                    {0,1,0,1,0,0,1,0,1,0}, // 0 - стена
                    {0,1,0,1,0,1,1,0,1,0}, // любое дpугое число-
                    {0,1,0,0,0,0,0,0,1,0}, // степень пpоходимости
                    {0,1,8,1,1,1,1,1,1,0}, // 1- наилучшая пpоходимость
                    {0,0,0,0,0,0,0,0,0,0}
            };

            // создаем экземпляр класса PathFinder и отправляем на вход массив labyrinth
            PathFinder pathFinder = new PathFinder(labyrinth);
            Point start = pathFinder.new Point(1,1);// Hачальная точка
            Point end = pathFinder.new Point(3,3);//Конечная точка
            // создаем одномерный массив типа Point и присвоим в каждый элемент координаты точек кратчайшего пути
            Point[] path = pathFinder.find(start,end); // Hайдем путь

            //выведем на печать координаты точек кратчайшего пути
            for(Point p: path)
                System.out.println(p);
        }
    }

}
