package day13_assignment;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MultipleChoiceQuestions {
	public static Quiz quiz = new Quiz();
	static Scanner scanner = new Scanner(System.in);
	public static boolean TimeUp = false;
	public static int score = 0;
	public static void main(String[] args) {
		List<Answer> answersList = new ArrayList<Answer>();
		Random randNum = new Random();
		ExecutorService es = Executors.newFixedThreadPool(2);
		addQuestions();
		System.out.println(quiz.getQuestions().size());
		es.execute(()->{
			ArrayList<Integer> randomIntArr = new ArrayList<Integer>();
			int quesCount = quiz.getQuestions().size();
			while(true) {
				if(!TimeUp && quesCount > 0) {
					int randomInt = randNum.nextInt(15);
					while(randomIntArr.contains(randomInt)) {
						randomInt = randNum.nextInt(15);
					}
					randomIntArr.add(randomInt);
					System.out.println("Random integer ==== "+randomInt);
					Question ques = quiz.getQuestions().get(randomInt);
					System.out.println("\n"+ques.getQuestion());
					System.out.println("\nOptions : ");
					for(int i=0;i<ques.getOptions().length;i++) {
						System.out.println((i+1)+" "+ques.getOptions()[i]);
					}
					System.out.println();
					System.out.println("\nPlease Enter your option number\n");
					int ans = scanner.nextInt();
					scanner.nextLine();
//					System.out.println(ans > 0 && ans <=4);
					while(!(ans > 0 && ans <=4)) {
						System.out.println("This is an invalid option. Please enter a valid one\n");
						ans = scanner.nextInt();
					}
					answersList.add(new Answer(ques.getQuesId(),ans));
					quesCount--;
					System.out.println("Questions will end in "+quesCount);
				}else {
					scanner.close();
					showResults(answersList);
					break;
				}
			}  
			es.shutdown();
		});
		
		
		es.execute(()->{
			Thread.currentThread().setName("Time Thread");
			new Reminder(900);
		});
	}

	
	public static void showResults(List<Answer> answersList) {
		List<Question> questionArr = quiz.getQuestions();
		System.out.println("Showing results");
		for(Answer answer:answersList) {
			int quesId = answer.getQuesID();
			for(Question ques:questionArr) {
				if(ques.getQuesId() == quesId) {
					if(ques.getAnswer() == answer.getAnswer()) {
						score++;
					}
				}
			}
		}
		
		System.out.println("*******************************");
		System.out.println("Final Score = "+score);
		System.out.println("*******************************");
		
	}
	
	private static void addQuestions() {
		quiz.addQuestion("If soccer is called football in England, what is American football called in England?", 1, setOptions("American football",
				"Combball","Handball","Touchdown"));
		
		quiz.addQuestion("What is the largest country in the world?", 1, setOptions("Russia","Canada","China","United States"));
		
		quiz.addQuestion("An organic compound is considered an alcohol if it has what functional group?", 2, setOptions("Hydroxyl","Carbonyl","Alkyl","Aldehyde"));
		
		quiz.addQuestion("What is the 100th digit of Pi?", 4, setOptions("9","4","7","2"));
		
		quiz.addQuestion("A doctor with a PhD is a doctor of what?", 2, setOptions("Philosophy","Psychology","Phrenology","Touchdown"));
		
		quiz.addQuestion("What year did World War I begin?", 3, setOptions("1914","1905","1919","1925"));
		
		quiz.addQuestion("What is isobutylphenylpropanoic acid more commonly known as?", 2, setOptions("AIbuprofen","Morphine","Ketamine","Aspirin"));
		
		quiz.addQuestion("What state is the largest state of the United States of America?", 2, setOptions("Alaska","California","Texas","Washington"));
		
		quiz.addQuestion("What is the tallest mountain in Canada?", 2, setOptions("Mount Logan","Mont Tremblant","Whistler Mountain","Blue Mountain"));
		
		quiz.addQuestion("Which of these is a stop codon in DNA?", 2, setOptions("TAA","ACT","ACA","GTA"));
		
		quiz.addQuestion("Which of these countries is NOT a part of the Asian continent?", 2, setOptions("Suriname","Georgia","Russia","Singapore"));
		
		quiz.addQuestion("What is the unit of currency in Laos?", 2, setOptions("Kip","Ruble","Konra","Dollar"));
		
		quiz.addQuestion("What is the name of the Canadian national anthem?", 1, setOptions("O Canada","O Red Maple","Leaf-Spangled Banner","March of the Puck Drop"));
		
		quiz.addQuestion("Which of these is NOT an Australian state or territory?", 1, setOptions("Alberta","New South Wales","Victoria","Queensland"));
		
		quiz.addQuestion("Where is the Luxor Hotel & Casino located?", 2, setOptions("Paradise, Nevada","Las Vegas, Nevada","Winchester, Nevada","Jackpot, Nevada"));
	}
	
	
	private static String[] setOptions(String option1,String option2,String option3,String option4) {
		String[] options =  {
				option1,
				option2,
				option3,
				option4
		};
		return options;
	}
	
	static class Reminder {
		Timer timer;

	    public Reminder(int seconds) {
	        timer = new Timer();
	        timer.schedule(new RemindTask(), seconds*1000);
		}

	    class RemindTask extends TimerTask {
	        public void run() {
	            System.out.println("Time's up!");
	            timer.cancel(); //Terminate the timer thread
	            TimeUp = true;
	        }
	    }
	}
}


interface Questions {
	public String getQuestion();
	public int getQuesId();
	public String[] getOptions();
	public int getAnswer();
}

class Question implements Questions {

	private int id;
	private String question;
	private int answer;
	private String[] options;
	
	public Question() {
		
	}

	public Question(int id,String question,int answer,String[] options) {
		this.id = id;
		this.question = question;
		this.answer = answer;
		this.options = options;
	}
	
	@Override
	public String getQuestion() {
		// TODO Auto-generated method stub
		return this.question;
	}

	@Override
	public int getQuesId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public String[] getOptions() {
		// TODO Auto-generated method stub
		return this.options;
	}

	@Override
	public int getAnswer() {
		// TODO Auto-generated method stub
		return this.answer;
	}
	
}

class Quiz {
	private Question question;
	public static List<Question> questionsList = new ArrayList<Question>();
	private static int id = 0;
	public Quiz() {
		
	}
	
	public void addQuestion(String ques,int answer,String[] options) {
		this.question = new Question(id,ques,answer,options);
		this.questionsList.add(question);
		id++;
	}
	
	public List<Question> getQuestions(){
		return this.questionsList;
	}
}

interface Answers {
	public int getQuesID();
	public int getAnswer();
}

class Answer implements Answers {
	private int quesId;
	private int answer;
	
	public Answer() {
		
	}
	
	public Answer(int quesId,int answer) {
		this.quesId = quesId;
		this.answer = answer;
	}
	
	@Override
	public int getQuesID() {
		// TODO Auto-generated method stub
		return this.quesId;
	}

	@Override
	public int getAnswer() {
		// TODO Auto-generated method stub
		return this.answer;
	}
	
}



















